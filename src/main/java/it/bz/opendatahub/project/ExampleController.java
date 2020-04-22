package it.bz.opendatahub.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.logstash.logback.argument.StructuredArguments.v;

@RestController
public class ExampleController {

    Logger logger = LoggerFactory.getLogger(ExampleController.class);

    @RequestMapping("/")
    public String index() {
        logger.info("We can log an ID as key-value pair for easy lookup in Log storage such as Elasticsearch.", v("id", 13));
        return "Hello World";
    }
}
