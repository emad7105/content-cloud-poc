package accountstates

default allow = false

allow {
#  input.accountState.attribute0 == token.payload.attribute0
  input.accountState.selectivity10 == token.payload.select_10
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
}