
# Branches

There is always one maintained branch for releases on the latest supported minecraft version. Anything added to this branch will be included in the next release.
Occasionally one or more feature branches will be maintained as well. The point of these are to accumulate new features into a larger update, or just to group features that for any other reason should be released together.

# Pull Requests

When you start development on a new feature or bugfix, you should as a rule of thumb create a new branch from the branch that you're targeting (see section on target branch below).
Once a pull request is done and merged, it is a good idea to delete the branch that got merged.

## Target branch

When making a bugfix, you should normally target the release branch (except of course when the bugfix is for a feature that is not on the release branch).
When making a feature, which branch you should target depends on if there is any active feature branch that is applicable for the feature in question.
If you're unsure, target the release branch or ask someone who is in charge of updates.

## Credit and changelog

When making a pull request, ensure that the relevant parties are credited.

With code changes, it is assumed that the commit author made those changes unless otherwise stated.
But for any assets, such as textures, sound files or models, please explicitly write out who the authors of these files are, even if you who are making the pull request is the author of the asset.
You can put this in the PR description or in the commit message.

Also, please make sure that you add any player-visible changes to the unreleased section of the [changelog](CHANGELOG.md).
Also make sure that those who contributed to the pull request are listed as contributors in the unreleased section.
