import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.log4j.Logger;

public class Main {
  static Logger LOG = Logger.getLogger(Main.class);

  public static void main(String... args) throws Exception {
    DefaultCamelContext defaultCamelContext = new DefaultCamelContext();

    RouteBuilder routeBuilder = new RouteBuilder() {
      @Override
      public void configure() throws Exception {
        from("netty4:tcp://localhost:8181?textline=true")
                .process(new Processor() {
                  public void process(Exchange exchange) throws Exception {
                    LOG.info("Rec: " + exchange.getIn().getBody());
                  }
                })
                .log(LoggingLevel.INFO, "out", "got ${body}") // This isn't getting logged
                .to("mock:result");
      }
    };

    defaultCamelContext.addRoutes(routeBuilder);

    defaultCamelContext.start();
  }
}
