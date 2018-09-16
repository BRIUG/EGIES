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
 * Implement a hierarchical place name model
 * 2017-07-01 15:35:44
 * michaelyangbo@outlook.com
 */
public class FullToponymyUtils {

	/*
	 * Implement a hierarchical place name model V1.0
	 */
	public static String[] getFullTopo(String str) {
		str = StringUtils.replaceBlank(str);
		str = SplitWordsUtils.hanLPSplit(str);
		String[] result = null;
		String temp = "";
		List<String> location = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;

		if (StringUtils.isEmpty(str)) {
			return result;
		} else {
			try {
				// Calling Hanlp can only segment words
				Segment segment = HanLP.newSegment().enablePlaceRecognize(true)
						.enableCustomDictionary(true)
						.enableOrganizationRecognize(true);
				List<Term> termList = segment.seg(str);
				List<String> list = new ArrayList<String>();
				if (termList.size() <= 1) {
					String s = termList.get(0).toString();
					s = s.replaceAll("/[a-zA-Z]+","" );  
					String expr = "select * from  t_toponymy where name like ?";
					conn = DataBaseUtils.getConnection();
					pstmt = conn.prepareStatement(expr);
					pstmt.setString(1, "%" + s + "%");
					rs = pstmt.executeQuery();
					if (rs.next()) {
						return rs.getString("name").split(",");
					} else {
						return s.split(",");
					}
				} else {
					for (int i = 0; i < termList.size(); i++) {
						String s = termList.get(i).toString();
						s = s.replaceAll("/[a-zA-Z]+","" );  
						String expr = "select * from  t_toponymy where name like ?";
						conn = DataBaseUtils.getConnection();
						pstmt = conn.prepareStatement(expr);
						pstmt.setString(1, "%" + s + "%");
						rs = pstmt.executeQuery();
						if (rs.next()) {
							list.add(rs.getString("id"));
							list.add(rs.getString("name"));
							list.add(rs.getString("type"));
						} else {
							temp += s;
							// temp = s;
						}
					}
					// One in the database, one in the database
					if (list.size() <= 3) {
						return (list.get(1) + temp).split(",");
					} else {
						int i = 1;
						while (i <= (list.size() - 3)) {
							if (list.get(i - 1) == list.get(i + 2)) {
								location.add(list.get(i));
							} else if (Integer.parseInt(list.get(i + 1)) == Integer
									.parseInt(list.get(i + 4))) {
								if (location.size() == 0) {
									location.add(list.get(i));
								} else {
									for (int j = 0; j < location.size(); j++) {
										if (location.get(j) != null) {
											if (location.toString().indexOf(
													list.get(i).toString()) == -1) {
												location.add(list.get(i));

											} else if (location
													.toString()
													.indexOf(
															list.get(i + 3)
																	.toString()) == -1) {
												location.add(list.get(i + 3));
											}
										}
									}
								}
							} else if (Integer.parseInt(list.get(i + 1)) < Integer
									.parseInt(list.get(i + 4))) {
								location.add(list.get(i) + list.get(i + 3));
								// 不包含
							} else if (location.toString().indexOf(
									list.get(i).toString()) == -1) {
								location.add(list.get(i));
							}
							i += 3;
						}
					}
					if (StringUtils.isNotEmpty(temp)) {
						location.add(temp);
					}
				}
				result = location.toArray((new String[location.size()]));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
			}
		}
		return result;
	}

	/*
	 * Implement a hierarchical place name model V2.0
	 */
	public static String[] getFullTopoSuper(String str) {
		String[] result = null;
		if (StringUtils.isEmpty(str)) {
			return result;
		} else {
			
		str = StringUtils.replaceBlank(str);
		str = SplitWordsUtils.hanLPSplit(str);
		List<String> location = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
			try {
				Segment segment = HanLP.newSegment().enablePlaceRecognize(true)
						.enableCustomDictionary(true)
						.enableOrganizationRecognize(true);
				List<Term> termList = segment.seg(str);
				if(termList.size() == 0){
					return str.split(",");
				}else if (termList.size() <= 1) {
					String s = termList.get(0).toString();
					s = s.replaceAll("/[a-zA-Z]+","" ); 
					String expr = "select * from  t_toponymy where name like ?";
					conn = DataBaseUtils.getConnection();
					pstmt = conn.prepareStatement(expr);
					pstmt.setString(1, "%" + s + "%");
					rs = pstmt.executeQuery();
					if (rs.next()) {
						return rs.getString("name").split(",");
					} else {
						return s.split(",");
					}
				} else {
					List<String> list = new ArrayList<String>();
					List<String> inDS = new ArrayList<String>();
					List<String> outDS = new ArrayList<String>();
					List<String> temp = new ArrayList<String>();
					for (int i = 0; i < termList.size(); i++) {
						list.add(termList.get(i).toString().replaceAll("/[a-zA-Z]+","" ));
					}

					for (int i = 0; i < list.size(); i++) {
						if (list.size() <= 1) {
							String s = list.get(i).toString();
							String expr = "select * from  t_toponymy where name like ?";
							conn = DataBaseUtils.getConnection();
							pstmt = conn.prepareStatement(expr);
							pstmt.setString(1, "%" + s + "%");
							rs = pstmt.executeQuery();
							if (rs.next()) {
								location.add(rs.getString("name"));

								inDS.add("" + i);
								inDS.add(rs.getString("id"));
								inDS.add(rs.getString("name"));
								inDS.add(rs.getString("type"));

								temp.add("" + i);
								temp.add(rs.getString("name"));
							} else {
								location.add(s);

								outDS.add("" + i);
								outDS.add(s);

								temp.add("" + i);
								temp.add(s);

							}
						} else if (list.size() <= 2) {
							String s = list.get(i).toString();
							String expr = "select * from  t_toponymy where name like ?";
							conn = DataBaseUtils.getConnection();
							pstmt = conn.prepareStatement(expr);
							pstmt.setString(1, "%" + s + "%");
							rs = pstmt.executeQuery();
							if (rs.next()) {
								if(inDS.size() == 0){
									if(location.size() == 0){
										location.add(rs.getString("name"));
									}
								}else{
									if (Integer.parseInt(inDS.get(i)) != Integer.parseInt(rs.getString("id"))) {
										if (Integer.parseInt(inDS.get(i + 2)) == Integer
												.parseInt(rs.getString("type"))) {
											location.add(rs.getString("name"));
										} else {
											if (Integer.parseInt(inDS.get(i + 2)) < Integer
													.parseInt(rs.getString("type"))) {
												location.set(0,
														(inDS.get(i + 1) + rs
																.getString("name")));
											}
										}
									}
								}

								inDS.add("" + i);
								inDS.add(rs.getString("id"));
								inDS.add(rs.getString("name"));
								inDS.add(rs.getString("type"));

								temp.add("" + i);
								temp.add(rs.getString("name"));
							} else {
								if (location.toString().indexOf(s) == -1) {
									location.set(0, (location.get(0) + s));
								}

								outDS.add("" + i);
								outDS.add(s);

								temp.add("" + i);
								temp.add(s);
							}
						} else {
							String s = list.get(i).toString();
							if(s.length() <=1){
								continue;
							}else{
								String expr = "select * from  t_toponymy where name like ?";
								conn = DataBaseUtils.getConnection();
								pstmt = conn.prepareStatement(expr);
								pstmt.setString(1, "%" + s + "%");
								rs = pstmt.executeQuery();
								if (rs.next()) {
									if (inDS.toString().indexOf(
											rs.getString("name")) == -1) {
										inDS.add("" + i);
										inDS.add(rs.getString("id"));
										inDS.add(rs.getString("name"));
										inDS.add(rs.getString("type"));
										
										temp.add("" + i);
										temp.add(rs.getString("name"));
									}
								} else {
									if (outDS.toString().indexOf(s) == -1) {
										outDS.add("" + i);
										outDS.add(s);
										
										temp.add("" + i);
										temp.add(s);
									}
								}
							}
						}
					}

					int j = 4;
					while (j <= (inDS.size() - 4)) {
						if ((Integer.parseInt(inDS.get(j - 4)) + 1) == Integer
								.parseInt(inDS.get(j))) {
							if (Integer.parseInt(inDS.get(j - 3)) == Integer
									.parseInt(inDS.get(j + 1))) {
								if (location.toString().indexOf(
										inDS.get(j - 2).toString()) == -1) {
									location.add(inDS.get(j - 2));
								}
							} else {
								if (Integer.parseInt(inDS.get(j - 1)) == Integer
										.parseInt(inDS.get(j + 3))) {
									if (location.toString().indexOf(
											inDS.get(j - 2).toString()) == -1) {
										location.add(inDS.get(j - 2));

									} else if (location.toString().indexOf(
											inDS.get(j + 2).toString()) == -1) {
										location.add(inDS.get(j + 2));
									}
								} else {
									if (Integer.parseInt(inDS.get(j - 1)) < Integer.parseInt(inDS.get(j + 3))) {
										float max = 0f;
										float ratio = 0f;
										for (int t = 0; t < location.size(); t++) {
											ratio = CharacterSimilarityUtils.getSimilarityRatio((inDS.get(j - 2)),location.get(t));
											if (ratio > max) {
												max = ratio;
											}
										}

										if (max < 0.2) {
											location.add(inDS.get(j - 2)+ inDS.get(j + 2));
										}
									} else {
										if (location.toString().indexOf(
												inDS.get(j - 2).toString()) == -1) {
											location.add(inDS.get(j - 2));
										}

										if (location.toString().indexOf(
												inDS.get(j + 2).toString()) == -1) {
											location.add(inDS.get(j + 2));
										}
									}
								}
							}
						}
						j += 4;
					}

					for (int i = 2; i < temp.size(); i += 2) {
						for (int k = 4; k <= inDS.size(); k += 4) {
							for (int l = 2; l <= outDS.size(); l += 2) {
								if (Integer.parseInt(temp.get(i - 2)) == Integer
										.parseInt(inDS.get(k - 4))) {
									if (Integer.parseInt(temp.get(i)) == Integer
											.parseInt(outDS.get(l - 2))) {
										if ((Integer.parseInt(temp.get(i - 2)) + 1) == Integer
												.parseInt(temp.get(i))) {
											float max = 0f;
											float ratio = 0f;
											int flag = 0;
											for (int t = 0; t < location.size(); t++) {
												ratio = CharacterSimilarityUtils
														.getSimilarityRatio(
																(temp.get(i - 1) + temp
																		.get(i + 1)),
																location.get(t));
												if (ratio > max) {
													max = ratio;
													flag = t;
												}
											}

											if (max < 0.2) {
												location.add(temp.get(i - 1)
														+ temp.get(i + 1));
											} else {
												location.set(flag,temp.get(i - 1)+ temp.get(i + 1));
											}
										}
									}
								} else {
									if (Integer.parseInt(temp.get(i - 2)) == Integer.parseInt(outDS.get(l - 2))) {
										if (Integer.parseInt(temp.get(i)) == Integer.parseInt(inDS.get(k - 4))) {
											if ((Integer.parseInt(temp.get(i - 2)) + 1) == Integer.parseInt(temp.get(i))) {
												float max1 = 0f;
												float ratio1 = 0f;
												for (int t = 0; t < location.size(); t++) {
													ratio1 = CharacterSimilarityUtils
															.getSimilarityRatio(
																	(temp.get(i - 1)),
																	location.get(t));
													if (ratio1 > max1) {
														max1 = ratio1;
													}
												}

												if (max1 < 0.2) {
													location.add(temp
															.get(i - 1));
												}

												float max2 = 0f;
												float ratio2 = 0f;
												for (int t = 0; t < location
														.size(); t++) {
													ratio2 = CharacterSimilarityUtils
															.getSimilarityRatio(
																	temp.get(i + 1),
																	location.get(t));
													if (ratio2 > max2) {
														max2 = ratio2;
													}
												}

												if (max2 < 0.2) {
													location.add(temp.get(i + 1));
												}
											}
										}
									}
								}
							}
						}
					}

					int k = 2;
					while (k <= (outDS.size() - 2)) {

						if ((Integer.parseInt(outDS.get(k - 2)) + 1) == Integer.parseInt(outDS.get(k))) {
							if (outDS.get(k - 1) == outDS.get(k + 1)) {
								float max = 0f;
								float ratio = 0f;
								for (int t = 0; t < location.size(); t++) {
									ratio = CharacterSimilarityUtils
											.getSimilarityRatio(
													(temp.get(k - 1)),
													location.get(t));
									if (ratio > max) {
										max = ratio;
									}
								}
								if (max < 0.2) {
									location.add(outDS.get(k - 1));
								}
							} else {
								float max1 = 0f;
								float ratio1 = 0f;
								for (int t = 0; t < location.size(); t++) {
									ratio1 = CharacterSimilarityUtils
											.getSimilarityRatio(
													(temp.get(k - 1)),
													location.get(t));
									if (ratio1 > max1) {
										max1 = ratio1;
									}
								}
								if (max1 < 0.2) {
									location.add(temp.get(k - 1));
								}
								float max2 = 0f;
								float ratio2 = 0f;
								for (int t = 0; t < location.size(); t++) {
									ratio2 = CharacterSimilarityUtils
											.getSimilarityRatio(
													temp.get(k + 1),
													location.get(t));
									if (ratio2 > max2) {
										max2 = ratio2;
									}
								}
								if (max2 < 0.2) {
									location.add(temp.get(k + 1));
								}
							}
						}else{
							if(Integer.parseInt(outDS.get(outDS.size()-2)) == Integer.parseInt(temp.get(temp.size()-2))){
								float max = 0f;
								float ratio = 0f;
								int flag = 0;
								for (int t = 0; t < location.size(); t++) {
									ratio = CharacterSimilarityUtils.getSimilarityRatio((outDS.get(outDS.size() - 1)),location.get(t));
									if (ratio > max) {
										max = ratio;
										flag = t;
									}
								}
								
								if (max < 0.2) {
									location.set(flag,location.get(flag)+outDS.get(outDS.size()- 1));
								}
							}
						}
						k += 2;
					}
				}
				result = location.toArray((new String[location.size()]));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
			}
		}
		return result;
	}
}
