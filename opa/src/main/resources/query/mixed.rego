package accountstates

default allow = false

allow {
  data.accountState.selectivity1 == token.payload.select_1
  data.accountState.brokerName == token.payload.broker
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}