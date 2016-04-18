<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to Web Video Converter</title>
	</head>
	<body>
		<div id="page-body" role="main">
			<div id="controller-list" role="navigation">
				<h3>Select video file to be converted:</h3>
				<g:form method="POST" action="convert" controller="VideoConversion" enctype="multipart/form-data">
				    <input type="file" name="videoFile" />
				    <input type="submit" value="Convert Video to Web" />
				</g:form>
			</div>
		</div>
	</body>
</html>
