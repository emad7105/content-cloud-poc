package accountstates

default allow = false

allow {
  input.accountState.brokerName == token.payload.broker
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}