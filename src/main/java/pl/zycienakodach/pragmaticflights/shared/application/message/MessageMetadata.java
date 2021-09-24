package pl.zycienakodach.pragmaticflights.shared.application.message;

import pl.zycienakodach.pragmaticflights.shared.application.tenant.TenantId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class MessageMetadata implements Map<String, String> {

  private static final String TENANT_ID_METADATA_KEY = "TenantId";
  private static final String CORRELATION_ID_METADATA_KEY = "CorrelationId";
  private static final String CAUSATION_ID_METADATA_KEY = "CausationId";
  private final HashMap<String, String> hashMap = new HashMap<>();

  protected MessageMetadata(TenantId tenantId, CorrelationId correlationId, CausationId causationId) {
    this.hashMap.put(TENANT_ID_METADATA_KEY, tenantId.raw());
    this.hashMap.put(CORRELATION_ID_METADATA_KEY, correlationId.raw());
    this.hashMap.put(CAUSATION_ID_METADATA_KEY, causationId.raw());
  }

  public TenantId tenantId(){
    return new TenantId(this.hashMap.get(TENANT_ID_METADATA_KEY));
  }

  public CorrelationId correlationId(){
    return new CorrelationId(this.hashMap.get(CORRELATION_ID_METADATA_KEY));
  }

  public CausationId causationId(){
    return new CausationId(this.hashMap.get(CAUSATION_ID_METADATA_KEY));
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
