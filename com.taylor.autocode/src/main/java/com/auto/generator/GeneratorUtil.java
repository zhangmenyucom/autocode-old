package com.auto.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.XMLParserException;

import com.auto.util.Constants;
import com.auto.util.DataUtils;
import com.auto.util.DateTools;
import com.auto.util.FreeMarkUtil;
import com.auto.util.RandomUtil;
import com.auto.util.StringTool;
import com.auto.util.TableColumnVo;

public class GeneratorUtil {

    private static Logger logger = Logger.getLogger(GeneratorUtil.class);

    /**
     * @notes:自动生成代码方法。可将其中的变更抽离出来，变更参数列表可有：
     * @configPath XML配置文件地址
     * @freemarkPath ftl模板库目录
     * @author 作者
     * @author_email 作者邮箱
     * @ognl_bean OGNL验证类全路径，如： com.taylor.api.mall.apartment.util.taylorOgnl
     * @blobTypeHandler 大字段处理类全路径，如：com.taylor.api.mall.util.BlobTypeHandler
     * @param args
     * @author xiaolu.z 2015-7-15 下午7:00:04
     */
    public static void main(String[] args) {
        try {
            logger.info("start generator ...");
            List<String> warnings = new ArrayList<String>();
            File configFile = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\database.xml");
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(configFile);
            List<Context> contexts = config.getContexts();

            // 初始化加载模板库
            FreeMarkUtil.init("\\src\\main\\resources");
            for (Context context : contexts) {
                Map<String, Object> fdata = new HashMap<String, Object>(0);
                fdata.put(Constants.OBJ_AUTHOR, "taylor");
                fdata.put(Constants.OBJ_AUTHOR_EMAIL, "516195940@qq.com");
                JDBCConnectionConfiguration jdbcConnectionConfiguration = context.getJdbcConnectionConfiguration();
                String jdbc_url = jdbcConnectionConfiguration.getConnectionURL();
                String jdbc_user = jdbcConnectionConfiguration.getUserId();
                String jdbc_pwd = jdbcConnectionConfiguration.getPassword();

                // Entity
                JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = context.getJavaModelGeneratorConfiguration();
                String entity_package = javaModelGeneratorConfiguration.getTargetPackage();
                String entity_file_path = javaModelGeneratorConfiguration.getTargetProject();
                fdata.put(Constants.OBJ_ENTITY_PACKAGE, entity_package);
                String entity_file_path_full = entity_file_path + "\\" + StringTool.getPackage2FilePath(entity_package);
                fdata.put(Constants.OBJ_ENTITY_FILE_PATH, entity_file_path_full);

                // Dao
                JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = context.getJavaClientGeneratorConfiguration();
                String dao_package = javaClientGeneratorConfiguration.getTargetPackage();
                String dao_file_path = javaClientGeneratorConfiguration.getTargetProject();
                fdata.put(Constants.OBJ_DAO_PACKAGE, dao_package);
                fdata.put(Constants.OBJ_DAO_FILE_PATH, dao_file_path + "\\" + StringTool.getPackage2FilePath(dao_package));
                fdata.put(Constants.OBJ_DAO_IMPL_PACKAGE, dao_package + ".impl");
                fdata.put(Constants.OBJ_DAO_IMPL_FILE_PATH, dao_file_path + "\\" + StringTool.getPackage2FilePath(dao_package + ".impl"));
                logger.info(fdata.get(Constants.OBJ_DAO_FILE_PATH));

                // Service
                String service_package = StringTool.getBasePackageByEntityPackage(entity_package) + ".service";
                String service_package_file_path = entity_file_path + "\\" + StringTool.getPackage2FilePath(service_package);
                String service_package_impl = service_package + ".impl";
                String service_package_impl_file_path = entity_file_path + "\\" + StringTool.getPackage2FilePath(service_package_impl);
                fdata.put(Constants.OBJ_SERVICE_PACKAGE, service_package);
                fdata.put(Constants.OBJ_SERVICE_FILE_PATH, service_package_file_path);
                fdata.put(Constants.OBJ_SERVICE_IMPL_PACKAGE, service_package_impl);
                fdata.put(Constants.OBJ_SERVICE_IMPL_FILE_PATH, service_package_impl_file_path);

                // Mapper
                SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = context.getSqlMapGeneratorConfiguration();
                String mapper_file_path = sqlMapGeneratorConfiguration.getTargetProject();
                fdata.put(Constants.OBJ_MAPPER_FILE_PATH, mapper_file_path);
                fdata.put(Constants.MAPPER_OGNL_BEAN, "com.taylor.api.common.util.TaylorOgnl");
                fdata.put(Constants.MAPPER_BLOB_HANDLER_BEAN, "com.taylor.api.common.util.BlobTypeHandler");

                List<TableConfiguration> tableConfigurations = context.getTableConfigurations();
                if (null != tableConfigurations && !tableConfigurations.isEmpty()) {
                    new DataUtils(jdbc_url, jdbc_user, jdbc_pwd);
                    for (TableConfiguration tableConfiguration : tableConfigurations) {
                        /**
                         * 自动生成实体类
                         */
                        Date date = new Date();
                        fdata.put(Constants.OBJ_CDATE, DateTools.formatDateTime(date, "yyyy-MM-dd HH:mm:ss"));
                        fdata.put(Constants.OBJ_NO, DateTools.formatDateTime(new Date(), "yyyyMMddHHmmss") + RandomUtil.getNumber(4));
                        String catalog = tableConfiguration.getCatalog();
                        String objectName = tableConfiguration.getDomainObjectName();
                        String tableName = tableConfiguration.getTableName();
                        fdata.put(Constants.OBJ_NOTES, catalog + "实体类");
                        fdata.put(Constants.OBJ_NAME, objectName);
                        Set<String> imports = new HashSet<String>(0);
                        List<TableColumnVo> tableColumnVos = DataUtils.getTableColumnList(tableName, imports);
                        fdata.put(Constants.OBJ_PROPERTYS, tableColumnVos);
                        fdata.put(Constants.OBJ_IMPORTS, imports);
                        FreeMarkUtil.autoFileByFtl("Entity.ftl", entity_file_path_full, objectName + ".java", fdata);

                        /***
                         * 自动生成Mapper文件
                         */
                        fdata.put(Constants.MAPPER_TABLE_NAME, tableName);
                        fdata.put(Constants.MAPPER_NAMESPACE, "mapper." + objectName);
                        String beanFullPackage = entity_package + "." + objectName;
                        fdata.put(Constants.MAPPER_RESULTMAP_TYPE, beanFullPackage);
                        FreeMarkUtil.autoFileByFtl("Mapper.ftl", mapper_file_path, objectName + "Mapper.xml", fdata);

                        /**
                         * 自动生成Dao接口及Dao实现类
                         */
                        imports.clear();
                        imports.add(beanFullPackage);// 实体全包路径
                        fdata.put(Constants.OBJ_NOTES, catalog + "Dao接口");
                        String daoName = objectName + "Dao";
                        fdata.put(Constants.OBJ_NAME, daoName);
                        fdata.put(Constants.OBJ_T_BEAN, objectName);
                        logger.info(fdata.get(Constants.OBJ_DAO_FILE_PATH));
                        FreeMarkUtil.autoFileByFtl("Dao.ftl", fdata.get(Constants.OBJ_DAO_FILE_PATH).toString(), objectName + "Dao.java", fdata);

                        String daoFullIntface = dao_package + "." + daoName;
                        imports.add(daoFullIntface);
                        fdata.put(Constants.OBJ_IMPL_BEAN, daoName);
                        fdata.put(Constants.OBJ_NOTES, catalog + "Dao实现类");
                        String daoImplDate = objectName + "DaoImpl";
                        fdata.put("OBJ_NAME", daoImplDate);
                        FreeMarkUtil.autoFileByFtl("DaoImpl.ftl", fdata.get(Constants.OBJ_DAO_IMPL_FILE_PATH).toString(), objectName + "DaoImpl.java", fdata);

                        /***
                         * 自动生成Service接口其实现类
                         */
                        imports.clear();
                        imports.add(beanFullPackage);// 实体全包路径
                        fdata.put(Constants.OBJ_NOTES, catalog + "Service接口");
                        String serviceName = objectName + "Service";
                        fdata.put(Constants.OBJ_NAME, serviceName);
                        FreeMarkUtil.autoFileByFtl("Service.ftl", fdata.get(Constants.OBJ_SERVICE_FILE_PATH).toString(), objectName + "Service.java", fdata);

                        imports.add(daoFullIntface);
                        String serviceFullIntface = service_package + "." + serviceName;
                        imports.add(serviceFullIntface);
                        fdata.put(Constants.OBJ_SERVICE_IMPL_PACKAGE, service_package_impl);
                        fdata.put(Constants.OBJ_DAO_NAME, daoName);
                        fdata.put(Constants.OBJ_DAO_NAME_PRO, StringTool.toLowerCaseIndex(daoName));
                        fdata.put(Constants.OBJ_IMPL_BEAN, serviceName);
                        fdata.put(Constants.OBJ_NOTES, catalog + "Service实现类");
                        String serviceImplName = objectName + "ServiceImpl";
                        fdata.put(Constants.OBJ_NAME, serviceImplName);
                        FreeMarkUtil.autoFileByFtl("ServiceImpl.ftl", fdata.get(Constants.OBJ_SERVICE_IMPL_FILE_PATH).toString(), serviceImplName + ".java", fdata);

                    }
                }

            }
            logger.info("generator finished! ...");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLParserException e) {
            e.printStackTrace();
        }
    }
}
