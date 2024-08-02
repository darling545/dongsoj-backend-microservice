package com.dongsoj.dongsojbackendcommon.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author dongs
 */
public class JwtUtils {

    private static final int TOKEN_TIME_OUT = 7 * 24 * 3600;

    // 加密token
    private static final String TOKEN_SECRET = "dongs";


    /**
     * 生成token
     * @param params
     * @return
     */
    public static String getToken(Map params){
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                .setExpiration(new Date(currentTime + TOKEN_TIME_OUT * 1000))
                .addClaims(params)
                .compact();
    }

    /**
     * 获取token中的claims信息
     * @param token
     * @return
     */
    public static Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(TOKEN_SECRET)
                .parseClaimsJws(token).getBody();
    }

    public static boolean verifyToken(String token){
        if (StringUtils.isEmpty(token)){
            return false;
        }

        try{
            Claims claims = Jwts.parser()
                    .setSigningKey("dongs")
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            return false;
        }

        return true;

    }

}
