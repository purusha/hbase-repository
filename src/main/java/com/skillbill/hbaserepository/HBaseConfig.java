package com.skillbill.hbaserepository;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.security.UserGroupInformation;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HBaseConfig {
    private final String rootDir;
    private final boolean withKerberos;
    private final String hbasePrincipal;    
    private final String hbaseRootdir;
    
    @Getter
    private final Configuration configuration;

    public HBaseConfig(String hbaseRootdir, String rootdir, boolean withKerberos, String hbasePrincipal) {
        this.hbaseRootdir = hbaseRootdir;
        this.rootDir = rootdir;
        this.withKerberos = withKerberos;
        this.hbasePrincipal = hbasePrincipal;
        this.configuration = buidConfiguration();
    }

    private Configuration buidConfiguration() {
        final Configuration conf = HBaseConfiguration.create();
        conf.set("hadoop.home.dir", hbaseRootdir);

        final Path path = new Path(rootDir + "/hbase-site.xml");
        LOGGER.info("load hbase configuration from: {}", path);
        conf.addResource(path);

        if (withKerberos) {
            LOGGER.info("USE HBase Principal");
            conf.set("hadoop.security.auth_to_local","RULE:[1:$0](APP)s///");
            UserGroupInformation.setConfiguration(conf);

            try {
                UserGroupInformation.loginUserFromKeytab(hbasePrincipal, rootDir + "/hbase.headless.keytab");
            } catch (IOException e) {
                LOGGER.error("Unable to connect to kerberos: ", e);
                throw new RuntimeException(e);
            }
        }

        conf.set("hbase.hconnection.threads.core", "0");
        conf.set("hbase.hconnection.threads.max", "0");

        dumpConfiguration(conf);
        return conf;
    }

    private void dumpConfiguration(Configuration conf) {
        LOGGER.debug("############################################");
        conf.forEach(e -> LOGGER.debug("{} => {}", e.getKey(), e.getValue()));
        LOGGER.debug("############################################");
    }
}
