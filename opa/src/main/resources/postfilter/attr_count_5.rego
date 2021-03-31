package accountstates

default allow = false

allow {
  input.accountState.attribute0 == token.payload.attribute0
  input.accountState.attribute1 == token.payload.attribute1
  input.accountState.attribute2 == token.payload.attribute2
  input.accountState.attribute3 == token.payload.attribute3
  input.accountState.attribute4 == token.payload.attribute4
}

token = {"payload": payload} {
  [header, payload, signature] := io.jwt.decode(input.token)
