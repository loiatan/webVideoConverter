<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to Web Video Converter</title>
	</head>
	<body>
		<div id="page-body" role="main">
			<h2>Play converted video now: </h2>
			<video width="320" height="240" controls>
			  <source src="${encodedVideoFileInfoMap.outputs[0].url}" type="video/mp4">
			Your browser does not support the video tag.
			</video>
			<h2>Job ${jobStatus.state}!</h2>
		</div>
		
	</body>
</html>
