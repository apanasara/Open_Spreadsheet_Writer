SELECT waytable.*
  FROM public."OSMxml",
       XMLTABLE ('/osm/way' PASSING (SELECT "WAY" FROM public."OSMxml" )
                 COLUMNS
                    id FOR ORDINALITY,
                    /*hotel_name text PATH '../../name' NOT NULL,*/
                    way_id bigint PATH '@id' NOT NULL
                    /*tagK text PATH '//nd[1]@ref'
                    tagV text PATH '//tag@V' NOT NULL
                    capacity int,
                    comment text PATH 'comment' DEFAULT 'A regular room'*/
                )as waytable; 

                
SELECT waytable.* FROM public."OSMxml", XMLTABLE ('/osm/way' PASSING (SELECT "WAY" FROM public."OSMxml" ) COLUMNS way_id bigint PATH '@id' NOT NULL)as waytable;

SELECT waytable.* FROM public."OSMxml", XMLTABLE ('/osm/way' PASSING (SELECT "WAY" FROM public."OSMxml" ) COLUMNS way_id bigint PATH '@id' NOT NULL)as waytable;

