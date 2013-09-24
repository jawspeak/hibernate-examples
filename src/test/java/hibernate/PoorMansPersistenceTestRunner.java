// Copyright 2013 Square, Inc.
package hibernate;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.hibernate.classic.Session;
import org.hibernate.jdbc.Work;
import org.junit.After;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import static java.nio.charset.Charset.forName;

public class PoorMansPersistenceTestRunner extends BlockJUnit4ClassRunner {
  public static final String RESET_DB_SQL_FILE = "./reset_db.sql";
  private Optional<Session> session = Optional.absent();

  public PoorMansPersistenceTestRunner(Class<?> klass) throws InitializationError {
    super(klass);
  }

  @Override
  protected Statement withBefores(FrameworkMethod method, final Object target,
      Statement statement) {
    session = Optional.of(HibernateUtil.getFactory().openSession());
    allowInjectionOfTheSession(target);
    hardResetDatabase();
    return super.withBefores(method, target, statement);
  }

  @Override
  protected Statement withAfters(FrameworkMethod method, final Object target, final Statement statement) {
    return new Statement() {
      @Override public void evaluate() throws Throwable {
        statement.evaluate();
        session.get().close();

        List<FrameworkMethod> afters = getTestClass().getAnnotatedMethods(After.class);
        for (FrameworkMethod after : afters) {
          after.invokeExplosively(target); // can't take any args
        }
      }
    };
  }

  private boolean findAnnotation(Field field, Class<InjectMe> search) {
    for (Annotation annotation : field.getDeclaredAnnotations()) {
      if (annotation.annotationType() == search) return true;
    }
    return false;
  }

  private void allowInjectionOfTheSession(Object target) {
    for (Field field : target.getClass().getDeclaredFields()) {
      if (findAnnotation(field, InjectMe.class) && field.getType() == Session.class) {
        try {
          field.set(target, session.get());
        } catch (IllegalAccessException e) {
          throw Throwables.propagate(e);
        }
      }
    }
  }


  private void hardResetDatabase() {
    session.get().doWork(new Work() {
      @Override public void execute(Connection connection) throws SQLException {
        // super hack - haha!
        try {
          List<String> lines = Files.readLines(new File(RESET_DB_SQL_FILE), forName("UTF-8"));
          String allSql = Joiner.on("\n").join(lines);
          for (String sql : Splitter.on(";").split(allSql)) {// poor man's
            if (!sql.trim().isEmpty()) connection.createStatement().execute(sql);
          }
        } catch (IOException e) {
          throw Throwables.propagate(e);
        }
      }
    });
  }
}
