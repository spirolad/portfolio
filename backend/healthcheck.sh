#!/bin/sh
set -eu

jshell <<'EOF' >/dev/null 2>&1
var url = new java.net.URL("http://localhost:8080/q/health/ready");
var conn = (java.net.HttpURLConnection) url.openConnection();
conn.setConnectTimeout(3000);
conn.setReadTimeout(3000);
conn.setRequestMethod("GET");
if (conn.getResponseCode() != 200) {
  throw new RuntimeException("Backend not ready");
}
EOF

