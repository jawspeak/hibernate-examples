// Copyright 2013 Square, Inc.
package hibernate.model;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name = "daily_summaries", catalog = "jaw_hbm")
public class DailySummary {

  @Id
  @Column(name = "id", unique = true)
  @GeneratedValue
  private Integer id;

  @Column(name = "date_at", nullable = false)
  private Date date;

  @Column(name = "volume", nullable = false)
  private Integer volume;

  // http://docs.oracle.com/javaee/6/api/javax/persistence/OneToOne.html
  @JoinColumn(name = "stock_id", nullable = false)
  @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Stock stock;

  public DailySummary() { }

  public DailySummary(Integer id, Integer volume) {
    this.id = id;
    this.volume = volume;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Integer getVolume() {
    return volume;
  }

  public void setVolume(Integer volume) {
    this.volume = volume;
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
