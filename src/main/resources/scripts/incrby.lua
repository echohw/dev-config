local key = KEYS[1];
local incrNum = ARGV[1];
local keyExists = redis.call("exists", key) == 1;
local value = 0;
if keyExists then value = redis.call("get", key) end;
if incrNum ~= nil then value = value + incrNum else value = value + 1 end;
redis.call("set", key, value);
return value;