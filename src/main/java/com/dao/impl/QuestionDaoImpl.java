package com.dao.impl;

import com.bean.Questionnaire;
import com.dao.QuestionDao;
import com.utils.JDBCUtils;

import java.sql.*;

public class QuestionDaoImpl implements QuestionDao {
    private Connection con;
    private PreparedStatement pStmt;
    private ResultSet rs;
    private String sql;



    public int newQuestionnaire(Questionnaire q)
    {
        //初始化
        con = null;
        pStmt = null;
        rs = null;

        int flag = 0;
        try
        {
            con = JDBCUtils.getConnection();
            sql = "INSERT INTO questionnaire(u_id, q_date, q_drugfreq, q_training,q_onsettimes) VALUES (?, ?, ?, ?, ?)";
            pStmt = con.prepareStatement(sql);

            pStmt.setInt(1, q.getU_id());
            pStmt.setDate(2, (Date) q.getQ_date());
            pStmt.setString(3, q.getQ_drugfreq());
            pStmt.setString(4, q.getQ_training());
            pStmt.setString(5, q.getQ_onsettimes());

            flag = pStmt.executeUpdate();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            JDBCUtils.closeAll(null, pStmt, con);
        }
        return flag;
    }

    public int newQuestionnaire(String tel, java.util.Date date, String drugfreq, String training, String onset)
    {
        //初始化
        con = null;
        pStmt = null;
        rs = null;

        int flag = 0;
        try
        {
            int u_id=0;
            con = JDBCUtils.getConnection();
            sql="select u_id from user where u_tel=?";
            pStmt = con.prepareStatement(sql);
            pStmt.setString(1,tel);
            rs = pStmt.executeQuery();
            if (rs.next()){
                u_id=rs.getInt("u_id");
            }


            sql = "INSERT INTO questionnaire(u_id, q_date, q_drugfreq, q_training,q_onsettimes) VALUES (?, ?, ?, ?, ?)";
            pStmt = con.prepareStatement(sql);

            pStmt.setInt(1, u_id);
            pStmt.setDate(2, (Date) date);
            pStmt.setString(3, drugfreq);
            pStmt.setString(4, training);
            pStmt.setString(5, onset);

            flag = pStmt.executeUpdate();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            JDBCUtils.closeAll(null, pStmt, con);
        }
        return flag;
    }

    public Questionnaire getLatestQs(String tel){
        //初始化
        con = null;
        pStmt = null;
        rs = null;
        int id = 0;
        String df = null;
        String trainning = null;
        String onset = null;
        Date date=new Date(0,0,0);
        Questionnaire q = null;
        try
        {
            con = JDBCUtils.getConnection();
            sql = "SELECT * FROM questionnaire WHERE u_id =(select u_id from user where u_tel=?)";
            pStmt = con.prepareStatement(sql);

            pStmt.setString(1, tel);
            rs = pStmt.executeQuery();

            while (rs.next())
            {
                if(rs.getDate("q_date").compareTo(date)==1){
                    date=rs.getDate("q_date");
                    id=rs.getInt("u_id");
                    df=rs.getString("q_drugfreq");
                    trainning=rs.getString("q_training");
                    onset=rs.getString("q_onsettimes");
                }

            }

            q = new Questionnaire();
            q.setU_id(id);
            q.setQ_date(date);
            q.setQ_drugfreqt(df);
            q.setQ_training(trainning);
            q.setQ_onsettimes(onset);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            JDBCUtils.closeAll(rs, pStmt, con);
        }
        return q;
    }
}
