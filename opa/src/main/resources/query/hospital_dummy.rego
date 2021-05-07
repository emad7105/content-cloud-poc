package hospital

default allow = false

allow {
  data.hospitalbill.brokerName == token.payload.broker
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}