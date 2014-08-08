<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c-rt" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="uk.co.metoffice.beans.WeatherData"%>
<%@page import="java.util.List"%>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
  <head>
    <title>METO - Product criteria page</title>
    <jsp:include page="common/metadata.jsp" />
  </head>


  <body id="home">
    <c-rt:forEach var="aCookie" items="<%=request.getCookies()%>">
      <c:if test="${aCookie.name=='RetailPortal'}">
        <c:set var="clientId" value="${aCookie.value}"/>
      </c:if>
    </c-rt:forEach>
    <c:set var="url" value="/meto/WeatherData/${requestScope.fromDate}/${requestScope.toDate}/${requestScope.regionList}/${requestScope.dayList}/${clientId}" />

    <div id="wrap">
      <jsp:include page="common/header.jsp" />

      <div id="main-body">
        <div id="content">

          <!-- Page Content Starts Here -->
          <div class="mainheading" style="background: url(../img/browser.gif) no-repeat top right;">
            <h1>Page H1 title</h1>
          </div>

          <div class="chapter">
            <p style="text-align:center;"><strong>Description text</strong></p>
            <p>What is this page doing.</p>
            <h3>Meto product criteria selection</h3>
          </div>

          <div class="chapter">
            <table id="customers">
              <tr>
                <th>Date</th>
                <th>Regions</th>
                <th>Max Temp (�C)</th>
                <th>Pressure</th>
                <th>Max Temp day</th>
                <th>Min Temp day</th>
                <th>Max feels like day</th>
                <th>Min Temp night</th>
                <th>Sunshine hours</th>
                <th>Snow amount</th>
              </tr>
              <c:forEach items="${requestScope.list}" var="i">
                <tr>
                  <td><c:out value="${i.weatherDate}"></c:out> </td>
                  <td><c:out value="${i.regions}"></c:out></td>
                  <td><c:out value="${i.temperature}"></c:out></td>
                  <td><c:out value="${i.pressure }"></c:out></td>
                  <td> <c:out value="${i.day }"> </c:out></td>
                  <td> </td> 	<td> </td> <td> </td>		<td> </td>	<td> </td>
                </tr>
              </c:forEach>
            </table>
            <br/><br/>

            <table>
              <tr class="lowerTable">
                <td>
                  <form action="${url}">
                    <input type="submit" value="Export data"> </input>
                  </form>
                </td>
                <td>
                  <form action="/">
                    <input type="submit" value="Email to user"> </input>
                  </form>
                </td>
                <td>
                  <form action="/">
                    <input type="submit" value="Print"> </input>
                  </form>
                </td>
              </tr>
            </table>
          </div>

        <!-- Page Content Ends Here -->
        </div> <!-- end #content -->
      </div> <!-- end #main-body -->

      <jsp:include page="common/rightsidebar.jsp" />
      <jsp:include page="common/footer.jsp" />
    </div> <!-- end #wrap -->
  </body>
</html>

