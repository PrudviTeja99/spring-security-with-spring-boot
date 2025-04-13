* Filters are called one after another
* They order can be configured in SecurityFilterChain with addFilterBefore(filterObjectThatComesBefore, classNameThatComesAfter) and addFilterAfter(filterObjectThatComesAfter,classNameThatComesBefore) and addFilter(filterObjectThatComesAtSamePoint, calssNameThatComesAtSamePoint)

## Password
* Can be configured by defining a bean of PasswordEncoder which encodes or creates hash of password before saving to database or checks the hash with given password

## Security flow for login
* AuthenticationManger takes UsernamePasswordAuthenticationToken
* AuthenticationManger deligates the implementation to AuthenticationProvider
* One of the AuthenticationProvider s is DaoAuthenticationProvider
* DaoAuthenticationProvider uses UserDetailsService interface, which contains loadsUserDetailsByUsername
* DaoAuthenticationProvider uses PasswordEncoder for cheking password given by converting it into a hash and password hash fetched from database
* authenticate method in AuthenticationManger takes the Authentication object and throws error if not valid