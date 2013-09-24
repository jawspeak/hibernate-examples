/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package hibernate;

import hibernate.model.DailySummary;
import hibernate.model.ExtraStockData;
import hibernate.model.Stock;
import java.util.Date;
import org.hibernate.classic.Session;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(PoorMansPersistenceTestRunner.class)
public class StockTest {

  @InjectMe Session session;

  @Test public void lookupStock() throws Exception {
    session.beginTransaction();
    Stock stock = new Stock();
    stock.setCode("4333");
    stock.setName("FB");
    session.save(stock);
    session.getTransaction().commit();
    System.out.println("end: " + stock);
  }

  @Test public void lookupStock_oneToOne_canSetAssociated() throws Exception {
    // no transaction
    Stock stock = new Stock();
    stock.setCode("4333");
    stock.setName("FB");

    System.out.println("pre-1st-save: " + stock);
    assertNull(stock.getId());
    session.save(stock); // must save now b/c the summary requires a non-null stock_id, so we must save it here to get one.
    assertNotNull(stock.getId());

    ExtraStockData extra = new ExtraStockData();
    extra.setNote("something in here");
    stock.setExtraStockData(extra);
    session.save(stock); // NOTE: this does not cascade saving the daily summary.

    System.out.println("pre-2nd-save: " + stock);
    assertNull(extra.getId());
    assertNull(extra.getStock()); // maybe this shouldn't be null? maybe i am missing some reverse association.
    extra.setStock(stock);
    session.save(extra);

    System.out.println("after save: " + stock);
    assertNotNull(extra.getId());
    assertNotNull(extra.getStock());
  }

  @Test public void lookupStock_oneToMany_canSetAssociated() throws Exception {
    // no transaction
    Stock stock = new Stock();
    stock.setCode("4333");
    stock.setName("FB");
    assertNull(stock.getId());
    session.save(stock); // must save now b/c the summary requires a non-null stock_id, so we must save it here to get one.
    System.out.println("new stock: " + stock);
    assertNotNull(stock.getId());
    session.refresh(stock);

    DailySummary dailySummary = new DailySummary();
    dailySummary.setDate(new Date());
    dailySummary.setVolume(10);
    dailySummary.setStock(stock);
    session.save(dailySummary); // NOTE: this does not cascade saving the daily summary.

    session.refresh(stock);
    System.out.println("stock: " + stock);

    dailySummary = new DailySummary();
    dailySummary.setDate(new DateTime().now().plusDays(2).toDate());
    dailySummary.setStock(stock);
    dailySummary.setVolume(20);
    session.save(dailySummary);

    session.refresh(stock);
    System.out.println("stock: " + stock);
  }

  @Test public void lookupDailySummary() throws Exception {
    Stock stock = new Stock();
    stock.setCode("19");
    stock.setName("CREE");

    assertNull(stock.getId());
    session.save(stock);
    assertNotNull(stock.getId());

    DailySummary summary = new DailySummary();
    summary.setStock(stock);
    summary.setDate(new Date());
    summary.setVolume(9);

    assertNull(summary.getId());
    session.save(summary);
    assertNotNull(summary.getId());

    session.refresh(summary);
    System.out.println(summary.getId());
    System.out.println("end: " + summary);
  }
}
