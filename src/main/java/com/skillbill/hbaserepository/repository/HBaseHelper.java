package com.skillbill.hbaserepository.repository;

import java.lang.reflect.Field;
import java.util.UUID;

import org.apache.hadoop.hbase.util.Bytes;

class HBaseHelper {

    static Object toObject(Class<?> type, byte[] value) {
        final Object result;
        if(type.equals(Integer.class)) {
            result = Bytes.toInt(value);
        } else if(type.equals(Long.class)) {
            result = Bytes.toLong(value);
        } else if(type.equals(UUID.class)) {
            result = UUID.fromString(Bytes.toString(value));
        } else {
            result = Bytes.toString(value);
        }
        return result;
    }

    static byte[] toB(Field f, Object item) throws IllegalAccessException {
        final byte[] value;

        final Class<?> type = f.getType();
        if(type.equals(Integer.class)) {
            value = item != null ? HBaseHelper.toB((Integer) f.get(item)) : null;
        } else if(type.equals(Long.class)) {
            value = item != null ? HBaseHelper.toB((Long) f.get(item)) : null;
        } else if(type.equals(UUID.class)) {
            value = item != null ? HBaseHelper.toB((UUID) f.get(item)) : null;
        } else if(type.equals(String.class)) {
            value = item != null ? HBaseHelper.toB(String.valueOf(f.get(item))) : null;
        } else {
            throw new RuntimeException("No supported type: " + type);
        }
        return  value;
    }

    static byte[] toB(Object s) {
        final byte[] value;
        if(s instanceof Integer) {
            value = HBaseHelper.toB((Integer) s);
        } else if(s instanceof Long) {
            value = HBaseHelper.toB((Long) s);
        } else if(s instanceof UUID) {
            value = HBaseHelper.toB((UUID) s);
        } else if(s instanceof String) {
            value = HBaseHelper.toB(String.valueOf(s));
        } else {
            throw new RuntimeException("No supported type: " + s.getClass().getSimpleName());
        }
        return value;
    }

    private static byte[] toB(String s) {
        if (s != null) return Bytes.toBytes(s);
        else return new byte[0];
    }

    private static byte[] toB(Integer s) {
        if (s != null) return Bytes.toBytes(s);
        else return new byte[Bytes.SIZEOF_INT];
    }

    private static byte[] toB(Long s) {
        if (s != null) return Bytes.toBytes(s);
        else return new byte[Bytes.SIZEOF_LONG];
    }

    private static byte[] toB(UUID uuid) {
        if (uuid != null) return toB(uuid.toString());
        else return new byte[128];
    }

}
