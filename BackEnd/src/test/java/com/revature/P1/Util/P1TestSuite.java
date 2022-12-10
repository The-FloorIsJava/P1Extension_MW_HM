package com.revature.P1.Util;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class P1TestSuite {

    @Test
    public void test_getConnection_returnValidConnection_givenProvidedCredentialsAreCorrect(){
        try(Connection connection = ConnectionFactory.getConnectionFactory().getConnection()) {
            System.out.println(connection);
            Assert.assertNotNull(connection);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


}
