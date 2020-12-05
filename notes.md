1. Got some error while starting mysql after installing it using community server. Then uninstalled and tried web version which downloads content on the fly while installing. This was successful. I had to manually retry some steps.
2. Faced issue with converting string to LocalDate in TripsRequest object. Had to add Date serializer and deserializer
explicitly in UtilBeansConfig. @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) did not help.
3. Spring bean validations did not work properly. @NotNull worked but custom validations using @AssertTue did not work. The reason was method naming convention. method names should start with is or get. 
Added spring-boot-starter-validation. hibernate-validator need not be added explicitly.
4. Faced issue with apache poi dependency versions. Unable to import XSSFWorkbook class. Changing the version fixed the issue.
5. Tried to use H2 DB as persistent DB. Mysql queries as is did not work. Single quotes for column alias created issue.
Replacing them with double quotes fixed syntax errors.
6. sum(imfl+beer) in slabwise group by query in mysql is BigDecimal type. It is coming as Long in H2 db. had to change code to extract this value from resultset.
Commnted mysql statement in ReportsService.
7. string constants in H2 DB should be single quotes. Column aliases should be double quotes. In mysql both work fine.
8. I think group by sums are Long in H2 DB (in mysql it is big decimal). Had to change json to replace abstract value types from bigDecimal to Long.
9. Simple column sums are int in H2 DB but Long in mysql DB. Had to outlet value types to int from Long.
