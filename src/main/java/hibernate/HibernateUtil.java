// Copyright 2013 Square, Inc.
package hibernate;

import com.google.common.base.Throwables;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
  private static final SessionFactory factory = buildSessionFactory();

  private static SessionFactory buildSessionFactory() {
    try {
      return new Configuration().configure().buildSessionFactory();
    } catch (Throwable ex) {
      System.err.println("Initial sessionFactory creation failed" + ex);
      throw Throwables.propagate(ex);
    }
  }

  public static SessionFactory getFactory() {
    return factory;
  }

  public static void shutdown() {
    getFactory().close();
  }
}
