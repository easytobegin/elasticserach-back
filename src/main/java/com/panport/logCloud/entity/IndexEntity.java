package com.panport.logCloud.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Objects;

/**
 * Created by minisheep on 2019/7/25.
 */
public class IndexEntity {
    private String health;
    private String status;
    private String index;
    private String uuid;
    private String pri;
    private String rep;
    @JSONField(name = "docs.count")
    private String count;
    @JSONField(name = "docs.deleted")
    private String deleted;
    @JSONField(name = "store.size")
    private String size;
    @JSONField(name = "pri.store.size")
    private String priSize;

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPri() {
        return pri;
    }

    public void setPri(String pri) {
        this.pri = pri;
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPriSize() {
        return priSize;
    }

    public void setPriSize(String priSize) {
        this.priSize = priSize;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexEntity that = (IndexEntity) o;
        return Objects.equals(health, that.health) &&
                Objects.equals(status, that.status) &&
                Objects.equals(index, that.index) &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(pri, that.pri) &&
                Objects.equals(rep, that.rep) &&
                Objects.equals(count, that.count) &&
                Objects.equals(deleted, that.deleted) &&
                Objects.equals(size, that.size) &&
                Objects.equals(priSize, that.priSize);
    }

    @Override
    public int hashCode() {

        return Objects.hash(health, status, index, uuid, pri, rep, count, deleted, size, priSize);
    }
}
