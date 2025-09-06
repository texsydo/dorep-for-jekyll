# Jekyll Site

Copy the following user files to the root of the Jekyll project before building
the Jekyll site:

**Mandatory**:

- `_data/site_meta.yml`
- `_data/footer.yml`
- `favicon.png`
- `favicon.ico`

**Recommended**:

- `README.md` of the user project.
- `LICENSE` of the user project (there can be many).

## Running the Static Site

It's recommended to use Linux, where you'll need to install Ruby, and the
project [Gemfile](Gemfile). The `bundle` tool will run Jekyll commands.

- `bundle exec jekyll clean`
- `bundle exec jekyll build`
- `bundle exec jekyll serve`

Distribution files go to [_site](_site).
