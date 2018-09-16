package casm.gis.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

/*
 * This class is mainly used to query the classifier of emergency information.
 * 2017-07-12 14:28:37
 */
public class SplitEmergencyTypeUtils {

	public static String[] getEmergencyType(String str) {
		str = StringUtils.replaceBlank(str);
		
		String[] result = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<String> inDS = new ArrayList<>();
		List<String> type = new ArrayList<>();
		if (StringUtils.isEmpty(str)) {
			return result;
		} else {
			try {
				Segment segment = HanLP.newSegment();
				List<Term> termList = segment.seg(str);
				for (int i = 0; i < termList.size(); i++) {
					String s = termList.get(i).toString();
					s = s.replaceAll("/[a-zA-Z]+","" );  
					String expr = "select * from  t_classifications where name like ?";
					conn = DataBaseUtils.getConnection();
					pstmt = conn.prepareStatement(expr);
					pstmt.setString(1, "%" + s + "%");
					rs = pstmt.executeQuery();
					if (rs.next()) {
						inDS.add(rs.getString("name"));
					}
				}
				if(inDS.size() != 0){
					float max = 0f;
					float ratio = 0f;
					int flag = 0;
					for (int i = 0; i < inDS.size(); i++) {
						ratio = CharacterSimilarityUtils.getSimilarityRatio(inDS.get(i),str);
						if (ratio > max) {
							max = ratio;
							flag = i;
						}
					}
					if (max > 0.1818) {
						if(type.size()==0){
							type.add(inDS.get(flag));
						}else{
							for (int j = 0; j < type.size(); j++) {
								if (type.toString().indexOf(inDS.get(flag).toString()) == -1) {
									type.add(inDS.get(flag));
								}
							}
						}
					}
					if(type.size() != 0){
						result = type.toArray((new String[type.size()]));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
			}
		}
		return result;
	}
}
