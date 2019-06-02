<%@page pageEncoding="UTF-8" %>
<%@ page import="main.Main"%>
<%
        String monthYear = request.getParameter("monthyear");
        String petitions = request.getParameter("petitions");
        String bool = request.getParameter("bool");
        String language = request.getParameter("language");
        String startHour = request.getParameter("startHour");
        String endHour = request.getParameter("endHour");
        
        String output = Main.main(monthYear,petitions,language,bool, startHour, endHour);    
        if (output.isEmpty()){
            output = "Durante el mes seleccionado no hay ninguna petición, así que no hay agenda que mostrar.";
        }

        %>

<!DOCTYPE html>
<html>
    <head>
        <title>Results</title>
        <link href="https://fonts.googleapis.com/css?family=Didact+Gothic&display=swap" rel="stylesheet">
        <style>div{margin-left: 80vw;margin-top: 10px; background-color:#e4e9f3; padding-left: 10px; padding-bottom: 5px; width: 100px;position: absolute; line-height: 1px;}td{text-align: center; vertical-align: middle;}h1, table, p{color: #333333;font-family:'Didact Gothic', sans-serif;}td{height:2em}table{table-layout: fixed;width:80vw;transition:0.5s}table:hover{box-shadow: 3px 5px #3f3f3f}table,th, td {border: 1px solid black;border-collapse: collapse;margin-left:auto;margin-right:auto;}</style>
    </head>
    <body bgcolor="MintCream">   
        <%= output %>
    </body>
</html>

