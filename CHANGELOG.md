**Version 0.2.2**
* Added resolutionCode to the standard task payload.

**Version 0.2.0**
* Persistent tasks no longer automatically save after the various updating methods are run.
* Persistent tasks no longer store a domain instance for the object lifecycle. Domains are now loaded, saved, and discarded.
* Domain interaction happens within new sessions to ensure that operations occur outside of transactions.
