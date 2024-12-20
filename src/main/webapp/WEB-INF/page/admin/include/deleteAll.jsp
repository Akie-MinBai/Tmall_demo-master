
<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.sql.*" %>
<html>
<head>
    <title>数据删除</title>
</head>
<body>
<%!
    public static final String DBDRIVER="com.mysql.cj.jdbc.Driver";
    public static final String DBURL="jdbc:mysql://localhost:3306/webstore?&useSSL=false&serverTimezone=UTC";
    public static final String DBUSER="root";
    public static final String DBPASS="123456";
%>
    <%
    Connection conn=null;
    PreparedStatement pst=null;
    int rs=0;
    String id=request.getParameter("id");
%>

    <%
   try{
	  Class.forName(DBDRIVER);
	  conn=DriverManager.getConnection(DBURL,DBUSER,DBPASS);
	  String sql_delete="delete from user_table where id="+id+"";
	  //获取要删除的此id的数据库信息
	  pst=conn.prepareStatement(sql_delete);
	  rs=pst.executeUpdate();
	  if(rs!=0){
		  out.println("删除成功");
%>
<jsp:forward page="select.jsp">
    <jsp:param name="ids" value="id"/>
</jsp:forward>
<%
        }
    }
    catch(Exception e){
        out.println(e);
    }

%>
</body>
</html>

