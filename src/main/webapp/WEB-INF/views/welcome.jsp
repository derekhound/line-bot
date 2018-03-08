<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Welcome</title>

  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
</head>

<body>

<section>
  <div class="pull-right" style="padding-right: 50px">
    <a href="?language=en_US">English</a> | <a href="?language=zh_TW">Chinese</a>
  </div>
</section>

<div class="jumbotron">
  <h1> ${greeting} </h1>
  <p> ${tagline} </p>
</div>

<br />

<div class="jumbotron">
  <h1><spring:message code="welcome.greeting" /></h1>
  <p><spring:message code="welcome.tagline" /></p>
</div>
</body>
</html>
