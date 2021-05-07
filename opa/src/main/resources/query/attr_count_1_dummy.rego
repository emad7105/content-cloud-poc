package accountstates

default allow2 = false

allow2 {
  data.accountState.attribute0 == token.payload.attribute0
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}