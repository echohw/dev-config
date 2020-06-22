local client_id = KEYS[1];
local time_unit = ARGV[1];
local keyExist = redis.call("exists", client_id) == 1;
if keyExist then
    redis.call("incr", client_id);
else
    redis.call("set", client_id, 1, "ex", time_unit);
end
return redis.call("get", client_id);