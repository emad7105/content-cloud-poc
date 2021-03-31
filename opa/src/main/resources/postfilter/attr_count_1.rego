package accountstates

default allow = false

allow {
  input.accountState.attribute0 == token.payload.attribute0
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}