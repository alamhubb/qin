package com.qin.types;

/**
 * Maven 仓库配置
 */
public class Repository {
    /** 仓库唯一标识 */
    private String id;
    
    /** 仓库地址 */
    private String url;
    
    /** 仓库名称 */
    private String name;
    
    /** 是否启用 release 版本，默认 true */
    private boolean releases = true;
    
    /** 是否启用 snapshot 版本，默认 false */
    private boolean snapshots = false;

    public Repository() {}

    public Repository(String url) {
        this.url = url;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isReleases() { return releases; }
    public void setReleases(boolean releases) { this.releases = releases; }

    public boolean isSnapshots() { return snapshots; }
    public void setSnapshots(boolean snapshots) { this.snapshots = snapshots; }
}
