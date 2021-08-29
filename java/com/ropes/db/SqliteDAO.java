package com.ropes.db;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
public class SqliteDAO {
    private static Connection con = null;
    public static void dbConnection() throws SQLException, ClassNotFoundException{
        Statement stmt = null;
        try{
            Class.forName("org.sqlite.JDBC");
        } catch(ClassNotFoundException ex){
            System.out.println("ドライバーが見つかりませんでした");
            throw ex;
        }

        try{
            con = DriverManager.getConnection("jdbc:sqlite:rope.db");
            stmt = con.createStatement();
//            System.out.println("このDBクラスが生成されたよ");
            stmt.executeUpdate("create table if not exists posts(" +
                    "id integer primary key," +
                    "title text," +
                    "content text," +
                    "created_at timestamp NOT NULL default(datetime(CURRENT_TIMESTAMP, 'localtime')))"
            );
        } catch(SQLException e) {
            throw e;
        }
    }

    public static ResultSet dataQuery(String query) throws SQLException, ClassNotFoundException{
        RowSetFactory rowSetFactory = RowSetProvider.newFactory();
        CachedRowSet rowSet = rowSetFactory.createCachedRowSet();

        Statement stmt = null;
        ResultSet resultSet = null;

        try{
            dbConnection();
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(query);
            rowSet.populate(resultSet);
        } catch (SQLException ex){
            System.out.println("検索に失敗しました");
            throw ex;
        } finally {
            if(resultSet != null){
                resultSet.close();
            }
            if(stmt != null){
                stmt.close();
            }
            if(con != null){
                con.close();
            }
        }
        return rowSet;
    }

    public static Integer createPost(String title) throws SQLException, ClassNotFoundException{
        PreparedStatement stmt = null;
        Integer res = null;
        String sql = "INSERT INTO posts (title, content, created_at) VALUES (?, ?, ?)";
        try{
            dbConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2,"");
            stmt.setString(3, "");
            res = stmt.executeUpdate();
        } catch (SQLException ex){
            System.out.println("登録に失敗しました");
            throw ex;
        }
        return res;
    }

    public static void dbUpdate(String sql) throws SQLException, ClassNotFoundException{
        Statement stmt = null;
        try{
            dbConnection();
            stmt = con.createStatement();
            stmt.execute(sql);
        } catch(SQLException ex){
            System.out.println(ex);
            System.out.println("データ処理に失敗しました");
        } finally {
            if(stmt != null){
                stmt.close();
            }
            if (con != null){
                con.close();
            }
        }
    }

    public static ResultSet readPost(Integer postId) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        ResultSet result = null;
        String sql = "SELECT * FROM posts WHERE id = " + postId + ";";
        try{
            dbConnection();
            stmt = con.createStatement();
            result = stmt.executeQuery(sql);
        } catch (SQLException ex){
            System.out.println("登録に失敗しました");
            throw ex;
        }
        return result;
    }

    public static ResultSet readPosts() throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        ResultSet result = null;
        String sql = "SELECT * FROM posts ";
        try{
            dbConnection();
            stmt = con.createStatement();
            result = stmt.executeQuery(sql);
        } catch (SQLException ex){
            System.out.println("登録に失敗しました");
            throw ex;
        }
        return result;
    }

    public static Integer deletePost(Integer postId) throws SQLException, ClassNotFoundException{
        PreparedStatement stmt = null;
        Integer res = null;
        String sql = "delete from posts where id =?";
        try{
            dbConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, postId);
            res = stmt.executeUpdate();
        } catch (SQLException ex){
            System.out.println("削除に失敗しました");
            throw ex;
        }
        return res;
    }

    public static void updatePost(Integer postId, String title, String content) throws SQLException, ClassNotFoundException{
        PreparedStatement stmt = null;
        String sql = "update posts set title =?, content =? where id =?";

        try{
            dbConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1,title);
            stmt.setString(2,content);
            stmt.setInt(3,postId);
            stmt.executeUpdate();
        } catch(SQLException ex){
            System.out.println(ex);
            System.out.println("データ処理に失敗しました");
        } finally {
            if(stmt != null){
                stmt.close();
            }
            if (con != null){
                con.close();
            }
        }
    }
}
