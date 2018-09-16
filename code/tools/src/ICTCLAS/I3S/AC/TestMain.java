package ICTCLAS.I3S.AC;
import ICTCLAS.I3S.AC.ICTCLAS50;
import java.util.*;
import java.io.*;


class TestMain{
	public static void main(String[] args)
	{
		try
		{
			String sInput = "随后温总理就离开了舟曲县城，预计温总理今天下午就回到北京。以上就是今天上午的最新动态，性价比很高。";
			testICTCLAS_ParagraphProcess(sInput);//同testimportuserdict和testSetPOSmap
//			testICTCLAS_FileProcess();

		}
		catch (Exception ex)
		{
		}
	}

	public static void testICTCLAS_ParagraphProcess(String sInput)
	{
		try
		{
			ICTCLAS50 testICTCLAS50 = new ICTCLAS50();
			String argu = ".";
			if (testICTCLAS50.ICTCLAS_Init(argu.getBytes("GB2312")) == false)
			{
				System.out.println("Init Fail!");
				return;
			}

			testICTCLAS50.ICTCLAS_SetPOSmap(2);

			byte nativeBytes[] = testICTCLAS50.ICTCLAS_ParagraphProcess(sInput.getBytes("GB2312"), 0, 1);//分词处理
			System.out.println(nativeBytes.length);
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
			System.out.println("未导入用户词典的分词结果： " + nativeStr);//打印结果

			int nCount = 0;
			String usrdir = "userdict.txt"; //用户字典路径
			byte[] usrdirb = usrdir.getBytes();//将string转化为byte类型
			nCount = testICTCLAS50.ICTCLAS_ImportUserDictFile(usrdirb, 0);
			System.out.println("导入用户词个数" + nCount);
			nCount = 0;

			byte nativeBytes1[] = testICTCLAS50.ICTCLAS_ParagraphProcess(sInput.getBytes("GB2312"), 2, 0);
			System.out.println(nativeBytes1.length);
			String nativeStr1 = new String(nativeBytes1, 0, nativeBytes1.length, "GB2312");
			System.out.println("导入用户词典后的分词结果： " + nativeStr1);
			testICTCLAS50.ICTCLAS_SaveTheUsrDic();
			testICTCLAS50.ICTCLAS_Exit();
		}
		catch (Exception ex)
		{
		}

	}



	public static void testICTCLAS_FileProcess()
	{
		try
		{
			ICTCLAS50 testICTCLAS50 = new ICTCLAS50();
			String argu = ".";
			if (testICTCLAS50.ICTCLAS_Init(argu.getBytes("GB2312")) == false)
			{
				System.out.println("Init Fail!");
				return;
			}

			String Inputfilename = "test.txt";
			byte[] Inputfilenameb = Inputfilename.getBytes();

			String Outputfilename = "test_result.txt";
			byte[] Outputfilenameb = Outputfilename.getBytes();

			testICTCLAS50.ICTCLAS_FileProcess(Inputfilenameb, 0, 0, Outputfilenameb);

			int nCount = 0;
			String usrdir = "userdict.txt";
			byte[] usrdirb = usrdir.getBytes();
			nCount = testICTCLAS50.ICTCLAS_ImportUserDictFile(usrdirb, 0);
			System.out.println("导入用户词个数" + nCount);
			nCount = 0;

			String Outputfilename1 = "testing_result.txt";
			byte[] Outputfilenameb1 = Outputfilename1.getBytes();

			testICTCLAS50.ICTCLAS_FileProcess(Inputfilenameb, 0, 0, Outputfilenameb1);
		}
		catch (Exception ex)
		{
		}
	}
}





