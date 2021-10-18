# README
To build use:
`mvn package`

To run use:
`./run`

To start the server use:
`./run --gui [--port=<port>]`

## Questions

As the article and the assignment state, women are more likely to understate their
good qualities and are more uncomfortable with self-promotion than men are.
This means that when it comes to team-matching (like in our project), and 
teams are made based on responders' own judgment of themselves, women will
generally rank themselves as less capable at some given task than men at the
same level will, which can result in biased and inaccurate group matching.
This can be extrapolated to other marginalized groups, such as BIPOC, who
may be less likely to self-promote.

In our model, which matches people together by how closely their characteristics 
are, people who self-report lower scores will be grouped with others who 
reported lower scores. In the cases of people who self-report lower scores
than their capabilities truly reflect, they may be grouped with people below
their ability level, so their matches are not as accurate as those of their
peers (who reported higher self-judged scores). This may reiterate biases
against minority groups. 

In our system, we made sure to include the fields of marginalized groups, 
and whether people would prefer to be with people of similar backgrounds.
However, as a generic model, our system does not account for changing 
self-reported scores to standardize them, since we don't have the information
to accurately change them. In the future, we could try to expand our model
to include such calculations. We could also, post-group-matching, look at
the diversity in the groups, and also try to balance it afterwards.
