// Copyright 2013 Square, Inc.
package hibernate.model;

import com.google.common.collect.Lists;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name = "stocks", catalog = "jaw_hbm")
public class Stock {

  @Id
  @Column(name = "id")
  @GeneratedValue
  private Integer id;

  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "name", nullable = false)
  private String name;

  @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Collection<DailySummary> dailySummary = Lists.newArrayList();

  @OneToOne(optional = true, mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private ExtraStockData extraStockData;

  public Stock() { }

  public Stock(Integer id, String code, String name) {
    this.id = id;
    this.code = code;
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Collection<DailySummary> getDailySummary() {
    return dailySummary;
  }

  public void setDailySummary(Collection<DailySummary> dailySummary) {
    this.dailySummary = dailySummary;
  }

  public ExtraStockData getExtraStockData() {
    return extraStockData;
  }

  public void setExtraStockData(ExtraStockData extraStockData) {
    this.extraStockData = extraStockData;
  }

  @Override public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
