<%-- 
    Document   : index
    Author     : Vimukthi Mudalige
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Google Drive Text File Uploader</title>
    </head>
    <body>
        <form id="fileUploadForm" method="post" action="fileUploadServlet">
	            <div class="form_group">
	                <label>Upload Text File</label>
                        <input type="file" name="fileUpload" />
	                <span id="fileUpload">Please select a text file</span>
	            </div>
	            <button id="uploadBtn" type="submit">Upload</button>
        </form>
    </body>
</html>
