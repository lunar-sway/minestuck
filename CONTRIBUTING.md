
# Branches

There is always one maintained branch for releases on the latest supported minecraft version. Anything added to this branch will be included in the next release.
Occasionally one or more feature branches will be maintained as well. The point of these are to accumulate new features into a larger update, or just to group features that for any other reason should be released together.
If there is a planned release date for content (such as 4/13), the feature branch `upcoming-content` is often utilized to accumulate new features. Content from independent or smaller pull requests are merged directly into `upcoming-content` whereas related pull requests may get their own feature branch if it becomes relevant. Such feature branches are when ready merged into `upcoming-content` or the release branch once finalized, and `upcoming-content` is merged into the release branch once the update that it is building towards is ready for release.
A feature branch is not always used, and is instead decided on a case-by-case basis. It should be assumed however that a Major Update will have its own feature branch.

# Pull Requests

When you start development on a new feature or bugfix, you should as a rule of thumb create a new branch from the branch that you're targeting (see section on target branch below).
Once a pull request is done and merged, it is a good idea to delete the branch that got merged.
If there are any technical issues with the pull request, it is recommended to talk with reviewers before you decide to close or recreate a pull request.

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

## Merging a Pull request

When a maintainer merges a branch or pull request, the type of merge that should be used depends on the branch being merged.

When merging a release branch or an accumulating feature branch into another branch, a regular merge commit should be made.

Otherwise, as is often the case, a PR should instead be squashed.
When squashing a PR, it is also a good idea to copy over any asset credits into the commit description.
This is strictly not necessary, and could be skipped as long as credit is written out clearly in the PR description.

In special cases where a PR consists of a single or a few self-contained commits, it is fine to merge by rebasing instead of squashing.
