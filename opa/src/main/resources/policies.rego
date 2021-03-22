package accountstates

default allow_partial = false

allow_partial {
  attr := data.accountState.attributes[_]
  attr.name == "clearanceLevel"
  attr.value == token.payload.clearanceLevel
}

default support_allow = false

support_allow {
    token.payload.broker == "broker0"
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}