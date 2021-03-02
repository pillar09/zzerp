package com.zzerp.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
public class FileUploadController {
	@Resource
	private ConfigDao configDao;

	@RequestMapping(value = "/shot", method = RequestMethod.GET)
	public String shot(@RequestParam(value = "billNo") String billNo, ModelMap model) {
		model.put("billNo", billNo);
		return "shot";
	}

	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public String uploadImage(
			@RequestParam("file") MultipartFile[] multipartFile,
			@RequestParam("billNo") String billNo, ModelMap model) {
		try {
			for (int i = 0; i < multipartFile.length; i++) {
				InputStream fileIn = multipartFile[i].getInputStream();
				String originalName = multipartFile[i].getOriginalFilename();
				if (originalName != null && originalName != "" && fileIn != null && billNo != null && billNo != "") {
					savePic(fileIn, originalName, billNo);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.put("billNo", billNo);
		return "shot";
	}

	private void savePic(InputStream inputStream, String fileName, String billNo) {
		String uploaded_file_dir = configDao.getDefine("uploaded_file_dir");
		OutputStream os = null;
		try {
			String path = uploaded_file_dir + billNo;
			// 2、保存到临时文件
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流保存到本地文件

			File tempFile = new File(path);
			if (!tempFile.exists()) {
				tempFile.mkdirs();
			}
			os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
			// 开始读取
			while ((len = inputStream.read(bs)) != -1) {
				os.write(bs, 0, len);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 完毕，关闭所有链接
			try {
				os.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
