package com.github.dolly0526.jessicalru.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author yusenyang
 * @create 2021/4/19 11:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LruRequest {

    private Integer oper;

    private String key;

    private Object value;


    public LruRequest(Integer oper, String key) {
        this.oper = oper;
        this.key = key;
    }

    @Getter
    @AllArgsConstructor
    public enum LruOper {

        CREATE(0),
        RETRIEVE(1),
        UPDATE(2),
        DELETE(3);

        public static LruOper matchOper(Integer oper) {
            for (LruOper lruOper : LruOper.values()) {
                if (lruOper.getOper().equals(oper))
                    return lruOper;
            }
            return RETRIEVE;
        }

        private Integer oper;
    }
}
