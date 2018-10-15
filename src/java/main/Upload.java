/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Vimukthi Mudalige
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

@WebServlet("/")
public class Upload extends HttpServlet {
    private static final long serialVersionUID = 1L;

	public Upload() {
		// TODO Auto-generated constructor stub
	}

	private static File _createGoogleFile(
			String googleFolderIdParent,
			String contentType, //
			String customFileName,
			AbstractInputStreamContent uploadStreamContent) throws IOException {

		File fileMetadata = new File();
		fileMetadata.setName(customFileName);

		List<String> parents = Arrays.asList(googleFolderIdParent);
		fileMetadata.setParents(parents);
		
		Drive driveService = GoogleDriveUtils.getDriveService();

		File file = driveService.files()
				.create(fileMetadata, uploadStreamContent)
				.setFields("id, webContentLink, webViewLink, parents")
				.execute();

		return file;
	}

	//Create Google File
	public static File createGoogleFile(String googleFolderIdParent,
			String contentType, //
			String customFileName, byte[] uploadData) throws IOException {
		
		AbstractInputStreamContent uploadStreamContent = new ByteArrayContent(
				contentType, uploadData);
		
		return _createGoogleFile(googleFolderIdParent, contentType,
				customFileName, uploadStreamContent);
	}

	//Create Google File from java.io.File
	public static File createGoogleFile(String googleFolderIdParent,
			String contentType, 
			String customFileName, java.io.File uploadFile) throws IOException {

		AbstractInputStreamContent uploadStreamContent = new FileContent(
				contentType, uploadFile);
		
		return _createGoogleFile(googleFolderIdParent, contentType,
				customFileName, uploadStreamContent);
	}

	//Create Google File from InputStream
	public static File createGoogleFile(String googleFolderIdParent,
			String contentType, 
			String customFileName, InputStream inputStream) throws IOException {

		AbstractInputStreamContent uploadStreamContent = new InputStreamContent(
				contentType, inputStream);
		
		return _createGoogleFile(googleFolderIdParent, contentType,
				customFileName, uploadStreamContent);
	}

	private String retrieveFileName(Part part) {
		String contentDisposition = part.getHeader("content-disposition");
		String[] items = contentDisposition.split(";");
		for (String str : items) {
			if (str.trim().startsWith("filename")) {
				return str.substring(str.indexOf("=") + 2, str.length() - 1);
			}
		}
		return "";
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String path = request.getParameter("fileUpload").replace("\\", "/");

		java.io.File uploadFile = new java.io.File(path);

		// Create Google File:
		File googleFile = createGoogleFile(null, "text/plain", "my_upload_text.txt",
				uploadFile);

		System.out.println("Online Link to View: " + googleFile.getWebViewLink());
		System.out.println("Upload to Drive Success!");
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("fileUploadResponse.jsp");

		dispatcher.forward(request, response);
	}
}
