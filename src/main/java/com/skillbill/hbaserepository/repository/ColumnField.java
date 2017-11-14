package com.skillbill.hbaserepository.repository;

import lombok.Value;

@Value
class ColumnField {

    byte[] name;
    byte[] family;
    byte[] value;
    Class<?> type;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();

        if (name !=  null) {
            sb.append("name => " + new String(name) + " ");
        }
        
        if (family !=  null) {
            sb.append("family => " + new String(family) + " ");
        }
        
        if (value !=  null) {
            sb.append("value => " + new String(value) + " ");
        }
        
        if (type !=  null) {
            sb.append("type => " + type.getSimpleName() + " ");
        }

        return sb.toString();
    }
}