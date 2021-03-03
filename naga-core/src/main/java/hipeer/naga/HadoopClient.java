package hipeer.naga;

import com.google.common.base.Strings;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.client.HdfsAdmin;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.Database;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.PrincipalType;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.net.URI;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HadoopClient {


    private Configuration configuration;
    private String hiveMetaStoreUris;
    private UserGroupInformation ugi;
    private String proxyUser;

    public HadoopClient(String proxyUser, String hadoopConfPath, String hiveMetaStoreUris){
        this.proxyUser = proxyUser;
        this.hiveMetaStoreUris = hiveMetaStoreUris;
        ugi = UserGroupInformation.createRemoteUser(proxyUser);
        this.configuration = new Configuration();
        this.configuration.addResource(new Path(String.format("%s/hdfs-site.xml", hadoopConfPath)));
        this.configuration.addResource(new Path(String.format("%s/core-site.xml", hadoopConfPath)));
    }


    public <T> T doPrivileged(PrivilegedExceptionAction<T> action, String realUser) throws IOException, InterruptedException {
        if(Strings.isNullOrEmpty(realUser)){
            return ugi.doAs(action);
        }

        UserGroupInformation proxyUser = UserGroupInformation.createProxyUser(realUser, ugi);
        return proxyUser.doAs(action);
    }

    public <T> T doPrivileged(PrivilegedAction<T> action, String realUser) {
        if(Strings.isNullOrEmpty(realUser)){
            return ugi.doAs(action);
        }
        String user;
        UserGroupInformation proxyUser = UserGroupInformation.createProxyUser(realUser, ugi);
        return ugi.doAs(action);
    }

    // 获取hdfs FileSystem
    public FileSystem getHadoopFileSystem(String realUser, String hdfsUri) throws IOException, InterruptedException {
        return doPrivileged(
                new PrivilegedExceptionAction<FileSystem>() {
                    @Override
                    public FileSystem run() throws Exception {
                        return FileSystem.newInstance(URI.create(hdfsUri), configuration);
                    }
        }, realUser);
    }


    // 获取hdfs admin
    public HdfsAdmin getHdfsAdmin(String hdfsUri) throws IOException, InterruptedException {

        return doPrivileged(
                new PrivilegedExceptionAction<HdfsAdmin>() {
                    @Override
                    public HdfsAdmin run() throws Exception {
                        return new HdfsAdmin(URI.create(hdfsUri), configuration);
                    }
                }, null);
    }


    // 创建hive数据库
    public Object createHiveDataBase(String dbName, String dbPath, String desc, String realUser) throws IOException, InterruptedException {
        return doPrivileged(
                new PrivilegedExceptionAction<Object>() {
                    @Override
                    public Object run() {
                        HiveConf hiveConf = new HiveConf();
                        hiveConf.setIntVar(HiveConf.ConfVars.METASTORETHRIFTCONNECTIONRETRIES, 3);
                        hiveConf.setVar(HiveConf.ConfVars.METASTOREURIS, hiveMetaStoreUris);
                        HiveMetaStoreClient hiveMetaStoreClient = null;
                        try {
                            hiveMetaStoreClient = new HiveMetaStoreClient(hiveConf);
                            Database db = new Database();
                            db.setName(dbName);
                            db.setDescription(desc);
                            db.setLocationUri(dbPath);
                            db.setOwnerName(realUser);
                            db.setOwnerType(PrincipalType.USER);
                            hiveMetaStoreClient.createDatabase(db);
                        } catch (Exception e) {
                            throw  new RuntimeException(e.getMessage());
                        } finally {
                            if(hiveMetaStoreClient != null) {
                                hiveMetaStoreClient.close();
                            }
                        }
                        return null;
                    }
                }, realUser);
    }


    // 获取所有表名
    public List<String> showTables(String dbName){
        HiveMetaStoreClient hiveMetaStoreClient = null;
        HiveConf hiveConf = new HiveConf();
        hiveConf.setIntVar(HiveConf.ConfVars.METASTORETHRIFTCONNECTIONRETRIES, 3);
        hiveConf.setVar(HiveConf.ConfVars.METASTOREURIS, hiveMetaStoreUris);
        try {
            hiveMetaStoreClient = new HiveMetaStoreClient(hiveConf);
            return hiveMetaStoreClient.getAllTables(dbName);
        } catch (MetaException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if(hiveMetaStoreClient != null){
                hiveMetaStoreClient.close();
            }
        }
    }


    // 获取表元数据信息
    public List<Map<String, String>> getTableSchemas(String dbName, String tableName){
        HiveMetaStoreClient hiveMetaStoreClient = null;
        HiveConf hiveConf = new HiveConf();
        hiveConf.setIntVar(HiveConf.ConfVars.METASTORETHRIFTCONNECTIONRETRIES, 3);
        hiveConf.setVar(HiveConf.ConfVars.METASTOREURIS, hiveMetaStoreUris);
        try {
            hiveMetaStoreClient = new HiveMetaStoreClient(hiveConf);
            List<FieldSchema> schemas = hiveMetaStoreClient.getSchema(dbName, tableName);
            return schemas.stream().map(col -> {
                Map<String, String> colInfo = new HashMap<>();
                colInfo.put("name", col.getName());
                colInfo.put("type", col.getType());
                colInfo.put("comment", col.getComment());
                return colInfo;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if(hiveMetaStoreClient != null){
                hiveMetaStoreClient.close();
            }
        }
    }

    public static void main(String[] args){

    }
}
