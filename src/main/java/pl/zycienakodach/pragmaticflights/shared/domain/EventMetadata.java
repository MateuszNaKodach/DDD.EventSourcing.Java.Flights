package pl.zycienakodach.pragmaticflights.shared.domain;

import pl.zycienakodach.pragmaticflights.shared.application.TenantId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class EventMetadata implements Map<String, String> {

  private static final String TENANT_ID_METADATA_KEY = "TenantId";
  private final HashMap<String, String> hashMap = new HashMap<>();

  public EventMetadata(TenantId tenantId) {
    this.hashMap.put(TENANT_ID_METADATA_KEY, tenantId.raw());
  }

  public TenantId getTenantId(){
    return new TenantId(this.hashMap.get(TENANT_ID_METADATA_KEY));
  }

  public int hashCode() {
    return hashMap.hashCode();
  }

  public int size() {
    return hashMap.size();
  }

  public boolean isEmpty() {
    return hashMap.isEmpty();
  }

  public String get(Object key) {
    return hashMap.get(key);
  }

  public String put(String key, String value) {
    return hashMap.put(key, value);
  }

  public boolean containsKey(Object key) {
    return hashMap.containsKey(key);
  }

  public String remove(Object key) {
    return hashMap.remove(key);
  }

  public void putAll(Map<? extends String, ? extends String> m) {
    hashMap.putAll(m);
  }

  public void clear() {
    hashMap.clear();
  }

  public boolean containsValue(Object value) {
    return hashMap.containsValue(value);
  }

  public Set<String> keySet() {
    return hashMap.keySet();
  }

  public Collection<String> values() {
    return hashMap.values();
  }

  public Set<Entry<String, String>> entrySet() {
    return hashMap.entrySet();
  }

  public boolean remove(Object key, Object value) {
    return hashMap.remove(key, value);
  }


}
