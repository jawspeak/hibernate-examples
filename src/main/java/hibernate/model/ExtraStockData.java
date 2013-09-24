// Copyright 2013 Square, Inc.
package hibernate.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * Demonstrates OneToOne, bidirectionally
 */
@Entity
@Table(name = "extra_stock_data", catalog = "jaw_hbm")
public class ExtraStockData {

  @Id
  @Column(name = "id", unique = true)
  @GeneratedValue
  private Integer id;

  @Column(name = "note", nullable = false)
  private String note;

  // http://docs.oracle.com/javaee/6/api/javax/persistence/OneToOne.html
  @JoinColumn(name = "stock_id", nullable = false)
  @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Stock stock;

  public ExtraStockData() { }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Stock getStock() {
    return stock;
  }

  public void setStock(Stock stock) {
    this.stock = stock;
  }

  @Override public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
