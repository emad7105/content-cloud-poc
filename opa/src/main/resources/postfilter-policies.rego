package accountstates

default allow = false

allow {
  [header, payload, signature] := io.jwt.decode(input.token)
  attrs := input.attributes
  some attr
  input.attributes[attr].name == "broker"
  input.attributes[attr].value == payload.broker
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}