package com.svvc.hackernews.model.restoutput;

public class ProfileUpdateRestInput {

  private String about;
  private String uemail;
  private Boolean showd;
  private Boolean nopro;
  private Integer maxv;
  private Integer mina;
  private Integer delay;

  public String getAbout() {
    return about;
  }

  public void setAbout(String about) {
    this.about = about;
  }

  public String getUemail() {
    return uemail;
  }

  public void setUemail(String uemail) {
    this.uemail = uemail;
  }

  public Boolean getShowd() {
    return showd;
  }

  public void setShowd(Boolean showd) {
    this.showd = showd;
  }

  public Boolean getNopro() {
    return nopro;
  }

  public void setNopro(Boolean nopro) {
    this.nopro = nopro;
  }

  public Integer getMaxv() {
    return maxv;
  }

  public void setMaxv(Integer maxv) {
    this.maxv = maxv;
  }

  public Integer getMina() {
    return mina;
  }

  public void setMina(Integer mina) {
    this.mina = mina;
  }

  public Integer getDelay() {
    return delay;
  }

  public void setDelay(Integer delay) {
    this.delay = delay;
  }
}
