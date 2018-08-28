package org.gmelo.hsbc.assignment.analytics.spark;

import com.mongodb.spark.MongoSpark;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.gmelo.hsbc.assignment.analytics.QueryService;
import org.gmelo.hsbc.assignment.common.exception.AssignmentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by gmelo on 17/08/18.
 * A spark standalone query service that runs on top of mongoDB
 */

@Service
public class SparkQueryService implements QueryService {

    private final Logger logger = LoggerFactory.getLogger(SparkQueryService.class);

    @Value("${org.gmelo.hsbc.mongo.url}")
    private String mongoUrl;
    @Value("${org.gmelo.hsbc.mongo.db}")
    private String mongoDb;
    @Value("${org.gmelo.hsbc.mongo.collection}")
    private String collection;
    //curently we are using a self contained spark module
    private String masterUrl = "local[*]";


    /**
     * Executes a query over a mongo db database.
     *  
     *
     * @param query a sql query to execute
     * @return the json return value
     */
    @Override
    public List<String> executeQuery(String query) {

        SparkSession spark = null;
        try {
            StringBuilder builder = new StringBuilder()
                    .append(mongoUrl)
                    .append("/")
                    .append(mongoDb)
                    .append(".")
                    .append(collection);
            spark = SparkSession.builder()
                    .master(masterUrl)
                    .appName("HSBC-ASSIGNMENT_QUERY")
                    .config("spark.mongodb.input.uri", builder.toString())
                    .getOrCreate();

            JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

            Dataset<Row> mongoDataStream = MongoSpark
                    .load(jsc)
                    .toDF()
                        /*
                         * Future optimization - pushing down the time slice to mongo to make the query more efficient and
                         *
                         * minimize the amount of data loaded by spark
                         * withPipeline(singletonList(Document.parse("{ $match:{ ts :{$gte:ISODate('%s'),$lte:ISODate('%s')}}}")))
                        */
                    .drop("_class")
                    .drop("_id");

            //protects from searching a DF with a non existing field
            if (mongoDataStream.count() == 0) {
                spark.close();
                return Collections.emptyList();
            }
            //the view to run the query on
            mongoDataStream.createOrReplaceTempView(collection);
            List<String> returned = spark.sql(query).toJSON().collectAsList();

            spark.close();
            return returned;
        } catch (Exception e) {
            logger.error("Exception while trying to execute query on spark ");
            throw new AssignmentException("Exception while trying to execute query on spark ", e);
        } finally {
            if (spark != null) {
                spark.close();
            }
        }

    }
}
