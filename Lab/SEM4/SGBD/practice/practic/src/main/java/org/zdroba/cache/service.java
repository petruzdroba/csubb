package org.zdroba.cache;

import org.ehcache.Cache;
import org.zdroba.DBConnection;
import org.zdroba.entity.dale;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class service {

    private final Cache<Long, dale> cache = CacheProvider.getCache();

    public dale getById(Long id){
        dale cached =  cache.get(id);

        if(cached != null){
            System.out.println("hit");
            return cached;
        }

        System.out.println("miss");

        try(Connection connection = DBConnection.getConnection()){

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM daleBeton WHERE id = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                dale dale2 =  new dale(
                        rs.getLong("id"),
                        rs.getString("tip"),
                        rs.getString("firma"),
                        rs.getInt("grosime"),
                        rs.getInt("pret")
                );

                cache.put(2L, dale2);
                return  dale2;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return cached;
    }
}
